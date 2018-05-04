#! /bin/bash

RED=`tput setaf 1`
RESET=`tput sgr0`

echo ""
echo "${RED}@@@@@@@@@@@@@@@ 바탕화면에 apk 만들기 @@@@@@@@@@@@@@@${RESET}"
echo ""

HERE="`dirname "$0"`"
cd ${HERE}

cd GPXTracker
rm -rf app/build/outputs/apk/*

printf "${RED}############ Release ############${RESET}"
echo ""
bash ./gradlew assembleRelease
mv app/build/outputs/apk/release/*.apk ~/Desktop

read -p "${RED}종료하려면 아무 키나 누르세요. 5초 뒤에 자동으로 닫힙니다.${RESET}" -n1 -s -t 5
