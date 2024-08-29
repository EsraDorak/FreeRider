###########################################################################
# Run-script that can be used by Visual Code Code Runner that runs code
# faster from compiled class files without packaging.
#

# source .env.sh to set CLASSPATH variable, if not already set
if [ -z "${CLASSPATH}" ]; then
    source .env.sh
fi

for arg in "$@"
do
    case "$arg" in
    "--force-compile")
        # compile project
        javac -cp ${CLASSPATH} $(find src/main/java -name '*.java') -d target/classes
        cp -R src/main/resources/* target/classes
    ;;
    esac
done

# run main
java -cp ${CLASSPATH} de.freerider.application.FreeriderApplication
