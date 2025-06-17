# HOW TO

At root of the project execute `.\gradlew jar`\
In a build/lib folder you'll find required jar. Execute it like requested in assignment.

# ASSUMPTIONS

In a real environment, these would be points to discuss with the business, but for the purpose of a test assignment, 
I've made assumptions based on the provided description only.

- Bonus symbol may not appear at all
- Not clear how bonus symbol probability calculated, so assumed each sell has 10% chance to be a bonus symbol which in
  turn is selected using config.json weights
- For the matrix larger than standard symbols quantity, [0:0] symbol is taken as a reference for missing symbols
- When multiple win combinations applicable in a group, only the one having larger multiplier selected, as only one combination allowed per group
- It is not said if same symbol count must match exactly to count of combination, so the assumption is symbol count must be at least as large
