echo ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
echo CYGWIN setup script
echo To run this you should:
echo + have JAVA_HOME defined
echo + have ANT installed
echo + either
echo + - run all commands, including this one, from the radar/extract directory, OR
echo + - define MINORTHIRD to be the radar/extract directory
echo ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
echo
export CLASSPATH="${CLASSPATH};.;${MINORTHIRD-.}/class;${MINORTHIRD-.}/lib;${MINORTHIRD-.}/lib/minorThirdIncludes.jar"
export CLASSPATH="${CLASSPATH};.;${MINORTHIRD-.}/lib/mixup;${MINORTHIRD-.}/config"
export MONTYLINGUA="${MINORTHIRD-.}/lib/montylingua"
