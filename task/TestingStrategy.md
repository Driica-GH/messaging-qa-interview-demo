High-level testing strategy for Contact endpoint.

1. Requirement gathering & analysis
   * Organizational:
     * Identify relevant stakeholders
     * Ensure tasks are created in project management tool
     * Ensure a project chat is created for quick communication between relevant parties
     * Review the existing product/technical documentation
   * Gather acceptance criteria from Product/Dev teams:
     * expected behavior
     * expected status codes
     * expected error codes
     * response structure 
   * Clarify missing information:
     * can either of the 3 parameters in createOrUpdate endpoint be missing, if one wants to only update lastname for example
     * is there a max length for firstname/lastname
     * are special characters/numbers allowed in firstname/lastname
     * is there a max length of contacts
     * what happens if all contacts are deleted and getContacts is called, does it throw error or returns empty list
     * how to avoid creating duplicate records of the same firstname/lastname but with different ids
     * how are records of contacts with multiple first and last names stored (e.g. one person's name: Justyna Sabina Joanna Smith-Jones)
2. Test Planning
   * Functional testing - are CRUD operations behaving as expected, positive and negative cases
   * Non-functional testing - performance, how does app behave when there is a really large amount of contacts
   * Exploratory testing - check for edge cases
   * Request a QA endpoint with capability to delete all records on non-production environments
3. Test documentation
   * create a high-level test cases that cover the known capabilities, can be in a form of checklist initially
   * expand the test cases as the clarifications for missing information gets available
   * add steps, expected behaviors, model schemas
4. Test Design
   * Preconditions - verify that the expected default contacts exist on App startup
   * Functional tests for each endpoint - validate schemas, case sensitivity, missing fields, special characters, edge cases
   * Non-functional tests - check response times on large data sets, for example GET /contact/allContacts when there are 5k/10k/20k records
5. Automation approach
   * API test automation - cover all functional flows
   * split and tag tests into regression/sanity/smoke test suites
   * integrate with CI
   * integrate with test reporting system and map to previously created test cases
6. Definition of Done - possible checklist
   * requirements clarified and documented, documentation is clear and up-to-date
   * unit tests done on Dev side
   * test cases created
   * API functional tests automated
   * edge cases and exploratory scenarios verified
   * non-functional tests covered
   * automated tests integrated with CI & reporting
   * regression suite successfully passes with no failures
   * test reports linked to original task, for visibility of coverage and results