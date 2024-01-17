/**
 * <h1>Module 'dependencies'</h1>
 * <p>
 * There are multiple ways to approach the issue of dependency management.
 * The goals for this project were the following:
 * </p>
 * <ul>
 *     <li>Compile time, not runtime: Have predictable behaviour that will not fail after shipping the app.</li>
 *     <li>Avoid annotation processing: This introduces a lot of complexity, so the benefits have to be enormous before I'd consider it here.</li>
 *     <li>Simplicity: duh...</li>
 * </ul>
 * <p>
 * With these criteria in mind, many solutions would not fit (Spring bc. of runtime, Dagger bc. of annotations & simplicity, ...).
 * This is why I chose the easiest approach possible:
 * Manage all dependencies as singleton instances in public fields of a context object.
 * This solution has alot of benefits:
 * </p>
 * <ul>
 *     <li>All "injections" are in one place</li>
 *     <li>Easy to test (in integration tests, you can choose whether you need the context, or need it initialized, or perform partial initialization yourself to fit the test scenario)</li>
 *     <li>As far away from "magic" as it can be</li>
 *     <li>Access to singleton values is straight forward as long as you have access to the context.</li>
 * </ul>
 */
package de.lpo.todo.dependencies;