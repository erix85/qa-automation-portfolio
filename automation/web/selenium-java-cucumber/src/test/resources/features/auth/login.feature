@login
Feature: Autenticación en DemoBlaze
  Como cliente registrado
  Quiero iniciar sesión con mis credenciales
  Para acceder a mi cuenta

  Background:
    Given que el usuario está en la página principal de DemoBlaze

  # ═══════════════════════════════════════════════════════════════════
  #  ESCENARIO POSITIVO PRINCIPAL (autocontenido con datos estáticos)
  # ═══════════════════════════════════════════════════════════════════
  @smoke @positive
  Scenario: Login exitoso con usuario registrado
    When hace click en el enlace "Log in"
    And el usuario "validUser" inicia sesión con sus credenciales
    Then debería ver el mensaje de bienvenida "Welcome qa_portfolio_demo"

  # ═══════════════════════════════════════════════════════════════════
  #  ESCENARIOS NEGATIVOS (autocontenidos)
  # ═══════════════════════════════════════════════════════════════════
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
    And el usuario "validUser" inicia sesión con contraseña incorrecta "WrongPassword123!"
    And confirma el login
    Then debería ver un alert con el mensaje "Wrong password."
    And acepta el alert

  # ═══════════════════════════════════════════════════════════════════
  #  ESCENARIO END-TO-END (integración signup → login, opcional)
  # ═══════════════════════════════════════════════════════════════════
  @e2e @ignore
  Scenario: Login exitoso tras crear cuenta nueva
    Given que el usuario está en la página principal de DemoBlaze
    When hace click en el enlace "Sign up"
    And completa el formulario de registro con un usuario único y contraseña "SecurePass123!"
    And confirma el registro
    Then debería ver un alert con el mensaje "Sign up successful."
    And acepta el alert
    When hace click en el enlace "Log in"
    And el usuario recién creado inicia sesión
    Then debería ver el mensaje de bienvenida "Welcome qa_user_"