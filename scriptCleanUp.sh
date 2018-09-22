#!/bin/sh

## This script cleans up the directory so that generated files don't get pushed to the git repository
## It was made since files are still being shown in the git status even though they were added to .gitignore

skipconf='f'

function usage(){
	echo
	echo "Cleans up the directory so that generated files don't get pushed to the git repository"
	echo "Usage : "
	echo "	-y : skip all of the confirmations"
	echo "	-h : show this menu"
}

if [ $# -gt 1 ]; then
	echo "Please do not give more than one argument to this script"
	usage
	exit 1
fi

if [[ $1 == '-h' ]]; then
	usage
	exit 0
elif [[ $1 == '-y' ]]; then
	echo "You have chosen to skip all confirmations, I hope you know what you're doing"
	skipconf="t"
elif [[ $# -eq 1 ]]; then
	echo "Your option couldn't be understood"
	usage
	exit 1
fi

echo "This script will clean up the directory depending on the .gitignore file"
if [[ $skipconf != 't' ]]; then
	read -p "Are you sure? (y/n) " -n 1 -r
	if [[ ! $REPLY =~ ^[Yy]$ ]]
	then
		exit 0
	fi
fi

echo
echo "Cleaning up cached files"

## Remove the file from the cache if it is in there
git ls-files > scriptCleanUp.txt

while IFS='' read -r line || [[ -n "$line" ]]; do
	if [[ $skipconf != 't' ]]; then
		grep "$line" scriptCleanUp.txt
		if [ $? -eq 0 ] ; then
			echo "Cleaning up "$line
			git rm -r --cached "$line"
		fi
	fi
done < ".gitignore"

rm scriptCleanUp.txt
