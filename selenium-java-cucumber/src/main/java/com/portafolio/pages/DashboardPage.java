package com.portafolio.pages;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Page Object para el Dashboard (inventario) de SauceDemo.
 *
 * <p><b>Responsabilidad:</b> modelar la página del dashboard: sus elementos,
 * acciones y validaciones específicas. Es la única clase autorizada a conocer
 * los locators del dashboard.</p>
 *
 * <p><b>¿Por qué es una clase aparte?</b> Porque cada página tiene sus propios
 * locators, acciones y validaciones. Mezclar responsabilidades (ej. validar
 * dashboard desde {@code LoginPage}) viola el Principio de Responsabilidad
 * Única (SRP) y hace que el código sea difícil de mantener.</p>
 *
 * <p><b>Herencia:</b> extiende {@link BasePage}, que le da acceso a operaciones
 * comunes (click, type, getText, isDisplayed, etc.).</p>
 *
 * <p><b>Página real:</b> {@code https://www.saucedemo.com/inventory.html}</p>
 *
 * @author Erick
 */
public class DashboardPage extends BasePage {

    // ═══════════════════ LOCATORS ═══════════════════

    /** Logo de la app (presente en todas las páginas autenticadas). */
    private static final By LOGO = By.className("app_logo");

    /** Título principal de la página (debe mostrar "Products"). */
    private static final By PAGE_TITLE = By.className("title");

    /** Contenedor del listado de productos. */
    private static final By INVENTORY_LIST = By.className("inventory_list");

    /** Cada tarjeta de producto individual. */
    private static final By INVENTORY_ITEMS = By.className("inventory_item");

    /** Nombre de cada producto. */
    private static final By ITEM_NAMES = By.className("inventory_item_name");

    /** Botón hamburguesa para abrir el menú lateral. */
    private static final By MENU_BUTTON = By.id("react-burger-menu-btn");

    /** Link de logout dentro del menú lateral. */
    private static final By LOGOUT_LINK = By.id("logout_sidebar_link");

    /** Ícono del carrito de compras. */
    private static final By SHOPPING_CART = By.className("shopping_cart_link");

    /** Badge numérico del carrito (muestra cuántos items hay). */
    private static final By CART_BADGE = By.className("shopping_cart_badge");

    public DashboardPage() {
        super();
        log.debug("DashboardPage instanciada");
    }

    // ═══════════════════ VALIDACIONES ═══════════════════

    /**
     * Verifica que el usuario está en el dashboard.
     * Es la validación principal que usan los steps de "Entonces".
     *
     * @return {@code true} si el dashboard está visible y cargado
     */
    public boolean isDashboardDisplayed() {
        try {
            boolean visible = isDisplayed(LOGO)
                           && isDisplayed(PAGE_TITLE)
                           && isDisplayed(INVENTORY_LIST);
            if (visible) {
                log.info("✅ Dashboard visible");
            } else {
                log.warn("⚠️ Dashboard parcialmente visible");
            }
            return visible;
        } catch (Exception e) {
            log.error("❌ Error verificando dashboard: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Devuelve el título de la página (usualmente "Products").
     */
    public String getPageTitle() {
        return getText(PAGE_TITLE);
    }

    /**
     * Cuenta cuántos productos hay listados en el dashboard.
     */
    public int getProductCount() {
        int count = countElements(INVENTORY_ITEMS);
        log.debug("📦 Productos encontrados: {}", count);
        return count;
    }

    /**
     * Devuelve la lista de nombres de todos los productos visibles.
     *
     * <p><b>Nota sobre null-safety:</b> se usa una lambda explícita en lugar de
     * un method reference ({@code WebElement::getText}) para evitar el warning
     * de conversión no chequeada que emite el null analysis de Eclipse/VS Code.</p>
     *
     * @return lista de nombres, o lista vacía si no hay productos
     */
    public List<String> getProductNames() {
        List<WebElement> elements = driver.findElements(ITEM_NAMES);
        if (elements.isEmpty()) {
            log.warn("⚠️ No se encontraron productos en el dashboard");
            return Collections.emptyList();
        }
        return elements.stream()
                .map(element -> element.getText())   // ← lambda explícita
                .collect(Collectors.toList());
    }

    /**
     * Verifica si un producto específico está listado.
     */
    public boolean isProductListed(String productName) {
        boolean listed = getProductNames().contains(productName);
        log.debug("🔍 ¿Producto '{}' listado? {}", productName, listed);
        return listed;
    }

    /**
     * Devuelve cuántos items hay en el carrito, o 0 si el badge no está visible.
     */
    public int getCartItemCount() {
        if (isDisplayed(CART_BADGE)) {
            String countText = getText(CART_BADGE);
            try {
                return Integer.parseInt(countText);
            } catch (NumberFormatException e) {
                log.warn("⚠️ Badge del carrito contiene un valor no numérico: '{}'", countText);
                return 0;
            }
        }
        return 0;
    }

    // ═══════════════════ ACCIONES ═══════════════════

    /**
     * Abre el menú lateral hamburguesa.
     */
    public void openMenu() {
        log.debug("🖱️ Abriendo menú lateral");
        click(MENU_BUTTON);
    }

    /**
     * Cierra sesión: abre el menú y hace clic en Logout.
     */
    public void logout() {
        log.info("🖱️ Cerrando sesión");
        openMenu();
        click(LOGOUT_LINK);
        log.info("✅ Sesión cerrada");
    }

    /**
     * Navega al carrito de compras.
     */
    public void goToCart() {
        log.debug("🛒 Navegando al carrito");
        click(SHOPPING_CART);
    }

    /**
     * Añade un producto al carrito por su nombre.
     * Cada producto tiene un botón "Add to cart" que se identifica por el
     * nombre del producto en el {@code data-test} del botón.
     *
     * @param productName nombre exacto del producto (ej. "Sauce Labs Backpack")
     */
    public void addProductToCart(String productName) {
        log.info("🛒 Añadiendo producto al carrito: {}", productName);

        // El id del botón es dinámico: "add-to-cart-<nombre-en-kebab-case>"
        String kebabName = productName.toLowerCase().replace(" ", "-");
        By addButton = By.id("add-to-cart-" + kebabName);

        click(addButton);
        log.info("✅ Producto '{}' añadido al carrito", productName);
    }

    // ═══════════════════ IMPLEMENTACIÓN DE BasePage ═══════════════════

    @Override
    public boolean isPageLoaded() {
        return isDashboardDisplayed();
    }
}