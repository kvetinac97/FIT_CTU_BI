#!/bin/sh
echo == init
php run.php init

echo == import tr1
php run.php import data/tr1.txt

echo == summaries
php run.php summary '111300' '0710' | diff -s test/111300_0710_1.txt -
php run.php summary '111323' '0710' | diff -s test/111323_0710_1.txt -
php run.php summary '330330' '0800' | diff -s test/330330_0800_1.txt -
php run.php summary '330331' '0800' | diff -s test/330331_0800_1.txt -

echo == import tr2
php run.php import data/tr2.txt

echo == summaries
php run.php summary '100100' '0300' | diff -s test/100100_0300_2.txt -
php run.php summary '100100' '0710' | diff -s test/100100_0710_2.txt -
php run.php summary '101010' '2010' | diff -s test/101010_2010_2.txt -
php run.php summary '111300' '0710' | diff -s test/111300_0710_2.txt -
php run.php summary '111323' '0710' | diff -s test/111323_0710_2.txt -
php run.php summary '330330' '0800' | diff -s test/330330_0800_2.txt -
php run.php summary '330331' '0800' | diff -s test/330331_0800_2.txt -
php run.php summary '330335' '0800' | diff -s test/330335_0800_2.txt -
