import {describe, expect, test} from '@jest/globals';
import {TypeHandler} from "@/services/typehandler";

/**
 * Sample class for testing.
 */
class TestClass {
}

/**
 * Sample class for testing.
 */
class AnotherClass {
}

const testClass = new TestClass()
const anotherClass = new AnotherClass();

describe("TypeHandler", () => {
    test("Call by 'on' condition when type match", () => {
        let calledOnForTestClass: boolean = false;
        let calledOnForAnotherClass: boolean = false;
        let calledElse: boolean = false;

        TypeHandler.handle(testClass)
            .on(TestClass, () => calledOnForTestClass = true)
            .on(AnotherClass, () => calledOnForAnotherClass = true)
            .else(() => calledElse = true);

        expect(calledOnForTestClass).toBeTruthy();
        expect(calledOnForAnotherClass).toBeFalsy();
        expect(calledElse).toBeFalsy();
    })

    test("Call by 'null' condition match", () => {
        let calledOn: boolean = false;
        let calledNull: boolean = false;
        let calledElse: boolean = false;

        TypeHandler.handle(null)
            .on(TestClass, () => calledOn = true)
            .onNull(() => calledNull = true)
            .else(() => calledElse = true);

        expect(calledOn).toBeFalsy();
        expect(calledElse).toBeFalsy();
        expect(calledNull).toBeTruthy();
    })

    test("Called by 'else' condition when no other match found", () => {
        let calledOn: boolean = false;
        let calledElse: boolean = false;

        TypeHandler.handle(anotherClass)
            .on(TestClass, () => calledOn = true)
            .else(() => calledElse = true);

        expect(calledOn).toBeFalsy();
        expect(calledElse).toBeFalsy();
    })
})