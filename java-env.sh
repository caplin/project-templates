!/bin/bash
#
# Set environment variables used by this blade's
# DataSource/bin/start-jar.sh script


# Java garbage collection log options
JVM_VERSION=$(java -version 2>&1 | head -1 | cut -d'"' -f2 | sed '/^1\./s///' | cut -d'.' -f1)
if [ $JVM_VERSION -le 8 ] ; then
    # Java 8: legacy logging
    GC_LOG_OPTIONS="-Xloggc:var/gc-%p.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=5M -XX:+PrintConcurrentLocks -XX:+PrintCommandLineFlags -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=var"
else
    # Java 11: JVM unified logging (introduced in Java 9)
    GC_LOG_OPTIONS="-Xlog:gc:file=var/gc-%p.log:tags,time,uptime,level:filecount=10,filesize=5M -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=var"
fi

##
# CAPLIN_BLADE_JAVA_OPTIONS
#
# Options in this variable are passed on the command line
# to the JVM that runs this blade.
#
# Example: set heap size to 512MB
#   CAPLIN_BLADE_JAVA_OPTIONS="-Xms512M -Xmx512M"
#
# Example: set heap size and enable GC logging
#   CAPLIN_BLADE_JAVA_OPTIONS="-Xms512M -Xmx512M ${GC_LOG_OPTIONS}"
#
export CAPLIN_BLADE_JAVA_OPTIONS=""