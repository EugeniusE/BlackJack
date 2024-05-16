@echo off

REM Execute clean, coverage, and test commands
call sbt clean coverage test

REM Generate coverage report
call sbt coverageReport
