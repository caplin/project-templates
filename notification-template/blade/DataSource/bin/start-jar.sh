#!/bin/bash
#
# Start the Notification Template Java Adapter.
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
   java -cp "${BINARY_ROOT}/lib/*" com.caplin.datasource.DataSource "$@"
   exit $?
else
   jar=$(ls "${BINARY_ROOT}"/lib/${BLADENAME}*.jar|head -1)
   echo "Jar: ${jar}"
   if [[ -n $START_FOREGROUND_NOLOGS ]]; then
      java $CAPLIN_BLADE_JAVA_OPTIONS -jar "$jar" "$@" > "$LOGDIR"/java-$BLADENAME.log 2>&1
   elif [[ -n $START_FOREGROUND ]]; then
      java $CAPLIN_BLADE_JAVA_OPTIONS -jar "$jar" "$@" --foreground-logs=true
   else
      java $CAPLIN_BLADE_JAVA_OPTIONS -jar "$jar" "$@" 2> "$LOGDIR"/java-$BLADENAME.log >/dev/null &
   fi
   echo $!
fi
