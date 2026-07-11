Feature: Autenticación en Portal Bancario
  Como cliente del banco
  Quiero iniciar sesión en el portal
  Para acceder a mis productos financieros

  @SmokeTest
  Scenario: Login exitoso con credenciales válidas
    Dado que el usuario está en la página de login
    Cuando ingresa usuario "standard_user" y contraseña "secret_sauce"
    Entonces el sistema redirige al dashboard

  @NegativeTest
  Scenario: Login fallido con credenciales inválidas
    Dado que el usuario está en la página de login
    Cuando ingresa usuario "invalid_user" y contraseña "wrong_password"
    Entonces muestra el mensaje de error "Username and password do not match any user in this service"
