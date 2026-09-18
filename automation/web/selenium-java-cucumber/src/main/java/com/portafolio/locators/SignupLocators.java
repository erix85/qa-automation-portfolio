package com.portafolio.locators;

import org.openqa.selenium.By;

/**
 * Locators del modal de Sign up de DemoBlaze.
 *
 * @author Erick
 */
public final class SignupLocators {

    private SignupLocators() {
        // Clase de constantes, no instanciable
    }

    public static final By MODAL          = By.id("signInModal");
    public static final By USERNAME_FIELD = By.id("sign-username");
    public static final By PASSWORD_FIELD = By.id("sign-password");
    public static final By SIGNUP_BUTTON  = By.xpath("//button[text()='Sign up']");
}