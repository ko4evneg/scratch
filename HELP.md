# HOW TO

RUN [ScratchGame.java](src/main/java/com/github/ko4evneg/ScratchGame.java)

# ASSUMPTIONS

In a real environment, these would be points to discuss with the business, but for the purpose of a test assignment, 
I've made assumptions based on the provided description only.

- Bonus symbol may not appear at all
- Bonus symbol probability in a cell is calculated as a part of (bonus + standard) symbol probabilities for this cell
- For the matrix larger than standard symbols quantity, [0:0] symbol is taken as a reference for missing symbols
- When multiple win combinations applicable in a group, only the one having larger multiplier selected, as only one combination allowed per group
- It is not said if same symbol count must match exactly to count of combination, so the assumption is symbol count must be at least as large
