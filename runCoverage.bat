@echo off

REM Execute clean, coverage, and test commands
call sbt clean coverage test

REM Generate coverage report
call sbt coverageReport


REM Open HTML file
start "" "target\scala-3.4.1\scoverage-report\overview.html"
