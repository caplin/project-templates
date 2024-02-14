#!/bin/bash
#
# Start the Pricing Template Java Adapter.
#
# $1 - Path to java executable
# $2 - Path to datasource XML file
# $3 - Path to fields XML file
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
   java $CAPLIN_BLADE_JAVA_OPTIONS -jar "$jar" "$@" 2> "$LOGDIR"/java-$BLADENAME.log >/dev/null &
   echo $!
fi
