#!/bin/bash
#
# Start the Blotter Template Java Adapter
#
# $1 - Path to java executable
# $2 - Path to datasource config file
# $3 - Path to fields config file
# $4 - Java definitions ( optional )
#
# Returns the process id of the Java process.
#

BLADENAME=@adapterName@

if [ "$1" = "CONFREADER" ]; then
   shift
   confreading=1
   jar=`ls "$BINARY_ROOT"/lib/datasource*.jar|head -1`
else
   confreading=0
   jar=`ls "$BINARY_ROOT"/lib/$BLADENAME*.jar|head -1`
   classpath="${BINARY_ROOT}/lib/*"

   echo "Classpath: $jar"
fi

if [[ -z $CAPLIN_GC_LOG ]]; then
  echo "jvm not defined, the gc log will not be shown."
else
  if [[ -z $gc_log_file_size ]]; then
    export gc_log_file_size=5
    echo 'the gc log fize size is '${gc_log_file_size}'M.'
  fi

  if [[ -z $number_Of_GCLogFiles ]]; then
    export number_Of_GCLogFiles=10
    echo 'the number of gc log files is '${number_Of_GCLogFiles}'.'
  fi
fi

if [ $confreading = 1 ]; then
   java -jar "$jar" "$@"
   exit $?
else
    if [[ ! -z $START_FOREGROUND_NOLOGS ]]; then
        if [ $CAPLIN_GC_LOG = "true" ]; then
            java -cp "$classpath" -jar -Xloggc:var/gc-%p.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=${gc_log_file_size} -XX:GCLogFileSize=${number_Of_GCLogFiles}M -XX:+PrintConcurrentLocks -XX:+PrintCommandLineFlags -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=var "$jar" "$@" > "$LOGDIR"/java$BLADENAME.log 2>&1
        else
            java -cp "$classpath" -jar "$jar" "$@" > "$LOGDIR"/java$BLADENAME.log 2>&1
        fi

    elif [[ ! -z $START_FOREGROUND ]]; then
        if [ $CAPLIN_GC_LOG = "true" ]; then
            java -cp "$classpath" -jar -jar -Xloggc:var/gc-%p.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=${gc_log_file_size} -XX:GCLogFileSize=${number_Of_GCLogFiles}M -XX:+PrintConcurrentLocks -XX:+PrintCommandLineFlags -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=var "$jar" "$@" --foreground-logs=true
        else
            java -cp "$classpath" -jar "$jar" "$@" --foreground-logs=true
        fi

    else
        if [ $CAPLIN_GC_LOG = "true" ]; then
            java -cp "$classpath" -jar -Xloggc:var/gc-%p.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=${gc_log_file_size} -XX:GCLogFileSize=${number_Of_GCLogFiles}M -XX:+PrintConcurrentLocks -XX:+PrintCommandLineFlags -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=var "$jar" "$@" 2> "$LOGDIR"/java-$BLADENAME.log >/dev/null &
        else
            java -cp "$classpath" -jar "$jar" "$@" 2> "$LOGDIR"/java-$BLADENAME.log >/dev/null &
        fi
    fi
   echo $!
fi
