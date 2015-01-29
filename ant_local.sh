#!/bin/sh
echo ' svn up ------------------------------'
svn up
echo ' build -------------------------------'
ant 
cp -f dist/client.jar /d/temp/public_alpha/
echo '--------------------------------------'
#echo 'upload to server...'
#/home/arksu/upload_client.sh

