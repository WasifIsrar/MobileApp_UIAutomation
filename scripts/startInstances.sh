#!/bin/bash
set -ex


# Define adb ports
recipe_uuid=$1
device_name=$2
adb_port=$3

# Start the gmsaas instance and capture its instance ID
instance_id=$(gmsaas --format json instances start $recipe_uuid $device_name | jq -r '.instance.uuid')

# Connect the instance to adb
gmsaas instances adbconnect $instance_id --adb-serial-port=$adb_port
echo "Connected instance to adb on port $adb_port"

# List adb devices to verify connections
adb devices

echo "::set-output name=instance_id::$instance_id"
