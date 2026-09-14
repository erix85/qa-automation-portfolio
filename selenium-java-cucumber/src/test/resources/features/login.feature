@login
Feature: Autenticación en Portal Bancario
  Como cliente del banco
  Quiero iniciar sesión en el portal
  Para acceder a mis productos financieros

  @SmokeTest
  Scenario: Login exitoso con credenciales válidas
    Dado que el usuario está en la página de login
    Cuando el usuario "validUser" inicia sesión
    Entonces el sistema redirige al dashboard

  @NegativeTest
  Scenario: Login fallido con credenciales inválidas
    Dado que el usuario está en la página de login
    Cuando el usuario "invalidUser" inicia sesión
    Entonces muestra el mensaje de error "Username and password do not match any user in this service"

  @NegativeTest
  Scenario: Login fallido con usuario bloqueado
    Dado que el usuario está en la página de login
    Cuando el usuario "lockedUser" inicia sesión
    Entonces muestra el mensaje de error "Sorry, this user has been locked out."