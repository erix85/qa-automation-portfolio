@signup
Feature: Registro de usuarios en DemoBlaze
  Como visitante de la tienda
  Quiero poder crear una cuenta nueva
  Para acceder a la tienda y comprar productos

  @smoke @positive
  Scenario: Registro exitoso con credenciales nuevas
    Given que el usuario está en la página principal de DemoBlaze
    When hace click en el enlace "Sign up"
    And completa el formulario de registro con un usuario único y contraseña "SecurePass123!"
    And confirma el registro
    Then debería ver un alert con el mensaje "Sign up successful."
    And acepta el alert
    And el usuario recién creado queda guardado para futuros tests
