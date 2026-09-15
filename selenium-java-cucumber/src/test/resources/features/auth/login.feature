@login
Feature: Autenticación en DemoBlaze
  Como cliente registrado
  Quiero iniciar sesión con mis credenciales
  Para acceder a mi cuenta

  Background:
    Given que el usuario está en la página principal de DemoBlaze

  @smoke @positive
  Scenario: Login exitoso con el usuario recién creado
    When hace click en el enlace "Log in"
    And el usuario recién creado inicia sesión
    Then debería ver el mensaje de bienvenida "Welcome qa_user_"

  @negative
  Scenario: Login fallido con usuario inexistente
    When hace click en el enlace "Log in"
    And completa el formulario de login con usuario "no_existe_user" y contraseña "WrongPass123!"
    And confirma el login
    Then debería ver un alert con el mensaje "User does not exist."
    And acepta el alert

  @negative
  Scenario: Login fallido con contraseña incorrecta
    When hace click en el enlace "Log in"
    And completa el formulario de login con un usuario generado y contraseña "WrongPassword"
    And confirma el login
    Then debería ver un alert con el mensaje "Wrong password."
    And acepta el alert