#!/bin/bash
start_logcat() {
    local log_file=$1
    local device_udid=$2
    local pid_file="logcat_pid_${device_udid}_$(basename "$log_file" .txt).txt"
    
    adb -s "$device_udid" logcat -v time > "$log_file" &
    LOGCAT_PID=$!

    # Check if logcat started successfully
    if ps -p $LOGCAT_PID > /dev/null; then
        echo $LOGCAT_PID > "$pid_file"
        echo "Logcat started with PID $LOGCAT_PID for $device_udid and logging to $log_file"
    else
        echo "Failed to start logcat for $device_udid."
    fi
}

stop_logcat() {
    local device_udid=$1
    for pid_file in logcat_pid_${device_udid}_*.txt; do
        if [ -f "$pid_file" ]; then
            LOGCAT_PID=$(cat "$pid_file")
            if ps -p $LOGCAT_PID > /dev/null; then
                kill $LOGCAT_PID
                echo "Stopped logcat with PID $LOGCAT_PID for $device_udid"
                rm "$pid_file"  # Clean up the PID file
            else
                echo "No such process with PID $LOGCAT_PID. It may have already stopped."
                rm "$pid_file"  # Clean up the PID file anyway
            fi
        else
            echo "PID file for device $device_udid not found."
        fi
    done
}

case "$1" in
    start)
        start_logcat "$2" "$3"
        ;;
    stop)
        stop_logcat "$2"
        ;;
    *)
        echo "Usage: $0 {start|stop} log_file device_udid"
        exit 1
esac