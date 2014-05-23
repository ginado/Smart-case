#!/bin/sh

CHEMIN_DISTANT='~/cours/fablab/SmartCaseApp'
LOGIN=meignanm
MACHINE=telesun

scp -r app/ conf/ public/ $LOGIN@$MACHINE.imag.fr:$CHEMIN_DISTANT

ssh $LOGIN@$MACHINE.imag.fr "cd $CHEMIN_DISTANT && ./activator clean compile && echo -n \"Compression de target.tar.gz ...\" && tar -czvf target.tar.gz target/ > /dev/null && echo \" terminee\""
rm -rf ./target
echo -n "Transfert de target.tar.gz ..."
scp $LOGIN@$MACHINE.imag.fr:$CHEMIN_DISTANT/target.tar.gz . > /dev/null
echo " termine"
echo -n "Decompression de target.tar.gz ..."
tar -xzvf target.tar.gz > /dev/null
echo " terminee"
rm -rf target.tar.gz

