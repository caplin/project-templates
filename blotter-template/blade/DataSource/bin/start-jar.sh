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

if [ $confreading = 1 ]; then
   java -jar "$jar" "$@"
   exit $?
else
   java -cp "$classpath" -jar $CAPLIN_BLADE_JAVA_OPTIONS "$jar" "$@" 2> "$LOGDIR"/java-$BLADENAME.log >/dev/null &
   echo $!
fi
