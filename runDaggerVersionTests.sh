#!/usr/bin/env bash

./gradlew --stop
./gradlew clean assemble

versions=(
    "2.4"
    "2.5"
    "2.6"
    "2.6.1"
    "2.7"
    "2.8"
    "2.9"
    "2.10"
    "2.11"
    "2.12"
    "2.13"
    "2.14"
    "2.14.1"
    "2.15"
    "2.16"
)
outDir="_dagger-test-results/`date +%s`"

failed=()
test_version() {
    echo "Test Dagger version: $1..."
    ./gradlew :demo:clean :demo:connectedDebugAndroidTest -PtestDaggerVersion=$1
    if [ $? -ne 0 ]
    then
        failed+=$1
        resultsDir="$outDir/$1"
        mkdir -p ${resultsDir}
        cp -r ./demo/build/outputs/androidTest-results ${resultsDir}
        cp -r ./demo/build/reports/androidTests ${resultsDir}
        echo "Test of $1 failed! See results at: $resultsDir"
    else
        echo "Test of $1 successful."
    fi
}

for version in "${versions[@]}"
do
    test_version ${version}
done

for version in "${failed[@]}"
do
    echo "Dagger $version failed - see results dir $outDir/$version"
done
