#!/bin/bash

function menu {
  echo    ================== WARNING ==================
  echo    Don\'t forget to commit first the modifications you want to keep.
  echo    It will reset all changes and remove new files that are not under version control
  echo
  while true; do
      read -p "Please tell me which step to move to. For instance 'Step2', or 'quit': " step
      case $step in
          Step* ) nextStep $step; break ;;
          quit|q ) exit;;
          * ) echo Didn\'t recognize $step, please choose one of Step0 - Step5;;
      esac
  done
}

function nextStep {
  step=$1
  echo -e "\n"
  echo  === Reset and Clean your working directory
  git reset --hard
  git clean -f
  echo -e "\n"
  echo  === Checkout $step
  git checkout $step
  echo -e "\n"
}

clear
menu
exit 0