#!/bin/bash
APP_NAME=impactor-production
URI=http://httpstat.us/200

ACCOUNT_ID_NEW_RELIC='3726713'
API_KEY_NEW_RELIC='NRAK-W0OT3B3ZG1V3DOOX9MDZR2YNDEK'
MONITOR_NAME="[availability] $APP_NAME"
CONDITION_NAME="$APP_NAME availability"
FREQUENCY=1440
POLICY_ID=3795567

API_KEY_STATUS_PAGE='12b156a6-87da-4494-9614-63d625e723e6'
STATUS_PAGE_ID='6b7n4crffrfg'
MICROSERVICES_GROUP_ID='4jjw4rfb10tx'
COMPONENT_NAME=$MONITOR_NAME
WORKFLOW_NAME="Notify Atlassian Status Page - $MONITOR_NAME"

echo "\nVerifying if synthetics monitor is empty in New Relic"
MONITOR_ID=$(curl -s -H "Api-Key:$API_KEY_NEW_RELIC" https://synthetics.newrelic.com/synthetics/api/v3/monitors | jq -r --arg MONITOR_NAME "$MONITOR_NAME" '.monitors[] | select(.name==$MONITOR_NAME) | .id')
if [ -z "$MONITOR_ID" ]; then #verify if MONITOR_ID is empty

    echo "\nCreating health monitor"
    curl -s --location --request POST 'https://synthetics.newrelic.com/synthetics/api/v3/monitors' \
        --header "Api-Key: $API_KEY_NEW_RELIC" \
        --header 'Content-Type: application/json' \
        --data-raw "{
            \"name\": \"$MONITOR_NAME\",
            \"frequency\": $FREQUENCY,
            \"uri\": \"$URI\",
            \"locations\": [
                \"AWS_SA_EAST_1\"
            ],
            \"type\": \"SIMPLE\",
            \"status\": \"enabled\",
            \"slaThreshold\": \"1.0\"
        }"
    MONITOR_ID=$(curl -s -H "Api-Key:$API_KEY_NEW_RELIC" https://synthetics.newrelic.com/synthetics/api/v3/monitors | jq -r --arg MONITOR_NAME "$MONITOR_NAME" '.monitors[] | select(.name==$MONITOR_NAME) | .id')
else
    echo "\nHealth monitor already exists"
    echo "\nThis is the existing monitor ID: $MONITOR_ID"
fi

sleep 10
echo "\nVerifying if alarm condition in alarm policy is empty in New Relic"
CONDITION_ID=$(curl -s -H "X-Api-Key:$API_KEY_NEW_RELIC" https://api.newrelic.com/v2/alerts_synthetics_conditions.json -d "policy_id=$POLICY_ID" | jq -r --arg MONITOR_ID "$MONITOR_ID" '.synthetics_conditions[] | select(.monitor_id==$MONITOR_ID) | .id')
if [ -z "$CONDITION_ID" ]; then #verify if CONDITION_ID is empty
    echo "\nCreating condition in Alarm Policy"
    curl -s --location --request POST "https://api.newrelic.com/v2/alerts_synthetics_conditions/policies/$POLICY_ID.json" \
        -H "X-Api-Key:$API_KEY_NEW_RELIC" \
        -H "Content-Type: application/json" \
        -d "{
        \"synthetics_condition\": {
        \"name\": \"$CONDITION_NAME\",
        \"monitor_id\": \"$MONITOR_ID\",
        \"enabled\": true
        }
    }"
    CONDITION_ID=$(curl -s -H "X-Api-Key:$API_KEY_NEW_RELIC" https://api.newrelic.com/v2/alerts_synthetics_conditions.json -d "policy_id=$POLICY_ID" | jq -r --arg MONITOR_ID "$MONITOR_ID" '.synthetics_conditions[] | select(.monitor_id==$MONITOR_ID) | .id')

else
    echo "\nAlarm Policy already exists"
fi



echo "\nVerifying if component exists in Atlassian Status Page"
AUTOMATION_EMAIL=$(curl -s https://api.statuspage.io/v1/pages/$STATUS_PAGE_ID/components -H "Authorization: OAuth $API_KEY_STATUS_PAGE" -X GET | jq -r --arg COMPONENT_NAME "$COMPONENT_NAME" '.[] | select (.name==$COMPONENT_NAME) | .automation_email')
if [ -z "$AUTOMATION_EMAIL" ]; then #verify if AUTOMATION_EMAIL is empty
    echo "\nCreating component in status page"
    AUTOMATION_EMAIL=$(curl -s --location --request POST "https://api.statuspage.io/v1/pages/$STATUS_PAGE_ID/components" \
        -H "Authorization: OAuth $API_KEY_STATUS_PAGE" \
        -d "component[description]=$CONDITION_NAME - Health Check" \
        -d "component[status]=operational" \
        -d "component[showcase]=true", \
        -d "component[start_date]=$(date +%F)", \
        -d "component[group_id]=$MICROSERVICES_GROUP_ID" \
        -d "component[name]=$COMPONENT_NAME" | jq -r .automation_email)

else
    echo "\nComponent already exists in status page"
fi
echo "\nAutomation email for status page: $AUTOMATION_EMAIL"

echo "\nVerifying if workflow for alarm in New Relic exists"
WORKFLOW_ID=$(curl -s https://api.newrelic.com/graphql \
    -H 'Content-Type: application/json' \
    -H "API-Key: $API_KEY_NEW_RELIC" \
    --data-binary "{\"query\":\"{ actor { account(id: $ACCOUNT_ID_NEW_RELIC) { aiWorkflows { workflows(filters: {name: \\\"$WORKFLOW_NAME\\\"}) { entities { id name } nextCursor totalCount } } } }}\", \"variables\":\"\"}" | jq -r --arg WORKFLOW_NAME "$WORKFLOW_NAME" '.data.actor.account.aiWorkflows.workflows.entities[] | select (.name==$WORKFLOW_NAME) | .id')
if [ -z "$WORKFLOW_ID" ]; then #verify if WORKFLOW_ID is empty

    echo "\nCreating email destination for workflow in new relic"
    DESTINATION_ID=$(curl -s https://api.newrelic.com/graphql \
        -H 'Content-Type: application/json' \
        -H "API-Key: $API_KEY_NEW_RELIC" \
        --data-binary "{\"query\":\"mutation { aiNotificationsCreateDestination(accountId: $ACCOUNT_ID_NEW_RELIC, destination: {type: EMAIL, name: \\\"Email to $AUTOMATION_EMAIL \\\", properties: [{key: \\\"email\\\", value: \\\"$AUTOMATION_EMAIL\\\"}]}) { destination { id name } }}\", \"variables\":\"\"}" | jq -r '.data.aiNotificationsCreateDestination.destination.id')

    echo "\nCreating email channel for workflow in new relic"
    CHANNEL_ID=$(curl -s https://api.newrelic.com/graphql \
        -H 'Content-Type: application/json' \
        -H "API-Key: $API_KEY_NEW_RELIC" \
        --data-binary "{\"query\":\"mutation { aiNotificationsCreateChannel(accountId: $ACCOUNT_ID_NEW_RELIC, channel: {type: EMAIL, name: \\\"Channel - Email to $AUTOMATION_EMAIL\\\", destinationId: \\\"$DESTINATION_ID\\\", product: IINT, properties: [{key: \\\"subject\\\", value: \\\"{{contains 'opened' stateText yes='OPENED' no='CLOSED'}}, {{ issueTitle }}\\\"}]}) { channel { id name } }}\", \"variables\":\"\"}" | jq -r '.data.aiNotificationsCreateChannel.channel.id')

    echo "\nCreating workflow in new relic to send health to atlassian status page"
    curl -s https://api.newrelic.com/graphql \
        -H 'Content-Type: application/json' \
        -H "API-Key: $API_KEY_NEW_RELIC" \
        --data-binary "{\"query\":\"mutation { aiWorkflowsCreateWorkflow(accountId: $ACCOUNT_ID_NEW_RELIC, createWorkflowData: {destinationsEnabled: true, workflowEnabled: true, name: \\\"$WORKFLOW_NAME\\\", issuesFilter: {name: \\\"Health check issue\\\", predicates: [{attribute: \\\"labels.policyIds\\\", operator: EXACTLY_MATCHES, values: [\\\"$POLICY_ID\\\"]}, {attribute: \\\"accumulations.conditionName\\\", operator: EXACTLY_MATCHES, values: [\\\"$CONDITION_NAME\\\"]}], type: FILTER}, destinationConfigurations: {channelId: \\\"$CHANNEL_ID\\\", notificationTriggers: [ACTIVATED, ACKNOWLEDGED, CLOSED]}, enrichmentsEnabled: true, enrichments: {nrql: []}, mutingRulesHandling: DONT_NOTIFY_FULLY_MUTED_ISSUES}) { workflow { id name destinationConfigurations { channelId name type notificationTriggers } enrichmentsEnabled destinationsEnabled issuesFilter { accountId id name predicates { attribute operator values } type } lastRun workflowEnabled mutingRulesHandling } errors { description type } }}\", \"variables\":\"\"}"
else
    echo "\nWorkflow in new relic for notifying Atlassian status page already exists"
fi
