#!/bin/bash
#
# Start the Trading Template Adapter Java DataSource.
#
# $1 - Path to java executable
# $2 - Path to datasource conf file
# $3 - Path to fields conf file
# $4 - Java definitions ( optional )
#
# Returns the process id of the Java process.
#

BLADENAME=@adapterName@

if [ "$1" = "CONFREADER" ]; then
   shift
   confreading=1
   jar=`ls "$BINARY_ROOT"/lib/datasource-java*.jar|head -1`
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
   java -cp "$classpath" -jar "$jar" --trading-property-file=etc/trading-provider.properties "$@" > "$LOGDIR"/java-$BLADENAME.log 2>&1 &
   echo $!
fi
