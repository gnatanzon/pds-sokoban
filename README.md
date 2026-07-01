# PDS-Sokoban
## La Guitarra de Lolo's Version

Implementación del juego **Sokoban** en Java con Swing, desarrollada como Trabajo Práctico para la materia Proceso de Desarrollo de Software (PDS).

---

## Requisitos

| Herramienta | Versión mínima |
|---|---|
| Java JDK | 17 o superior |
| IntelliJ IDEA (recomendado) 

---

## Cómo ejecutar

1. Abrir IntelliJ IDEA.
2. Ir a **File → Open** y seleccionar la carpeta `PDS-TPO/`.
3. Si lo pide, configurar el SDK: **File → Project Structure → Project → SDK → Java 17+**.
4. Localizar `src/Main.java` en el panel del proyecto.
5. Hacer clic derecho → **Run 'Main'**, o usar el botón ▶ de la barra superior.

> **Importante:** el juego debe ejecutarse siempre desde la carpeta `PDS-TPO/` como directorio de trabajo, ya que los archivos de nivel (`Nivel1.txt`, etc.) y los assets se buscan con rutas relativas desde ahí.

---

## Estructura del proyecto

```
PDS-TPO/
├── src/                        # Código fuente Java
│   ├── Main.java               # Punto de entrada
│   ├── controlador/            # ControladorJuego, GestorNiveles, Memento
│   ├── tablero/                # Tablero, Celda, CargadorNivel
│   │   ├── background/         # Tipos de piso (Pared, Destino, PisoResbaladizo…)
│   │   ├── entidades/          # Jugador, Caja y sus variantes, Entidad
│   │   │   ├── choque/         # Estrategias de choque (Inerte, Explosivo, Frágil)
│   │   │   ├── decorator/      # CajaDecorator, CajaAntirresbaladizaDecorator
│   │   │   ├── estado/         # EstadoFragil, EstadoResistente, EstadoRoto
│   │   │   └── movimiento/     # EstrategiaMovimiento, MovimientoNormal/Resbaladizo
│   │   └── constructor/        # FabricaElementos, FabricaElementosSokoban
│   ├── sonido/                 # GestorSonido (Singleton)
│   └── vista/                  # VentanaPrincipal, PanelTablero, PanelHUD, Selectores
├── Assets/
│   ├── *.png / *.PNG           # Sprites del juego
│   └── sonidos/                # Archivos de audio .wav
├── Nivel1.txt … Nivel7.txt     # Archivos de niveles
└── out/production/pds-sokoban/ # Clases compiladas
```

---

## Cómo jugar

### Flujo de pantallas

```
Selección de personaje → Selección de nivel → Partida → Resumen de nivel
```

Al terminar cada nivel, se muestra un resumen con movimientos, empujes y puntaje. Desde ahí se puede avanzar al siguiente nivel o volver al menú.

### Controles

| Tecla | Acción |
|---|---|
| `↑` `↓` `←` `→` o `W` `A` `S` `D` | Mover al jugador |
| Botón **Undo** (HUD) | Deshacer el último movimiento |
| Botón **Reiniciar** (HUD) | Reiniciar el nivel actual desde el principio |

### Objetivo

Empujar **todas las cajas** hasta ubicarlas sobre las **celdas destino** (marcadas en el mapa). El nivel se completa cuando todas las cajas están en su lugar.

---

## Formato de archivos de nivel

Cada archivo `NivelN.txt` sigue este formato:

```
MovMin=10
0-0 0-0 0-0 0-0 0-0
0-0 1-1 1-2 2-0 0-0
0-0 1-0 1-0 1-0 0-0
0-0 0-0 0-0 0-0 0-0
```

Cada celda se representa como `PISO-ENTIDAD`:

**Códigos de piso:**

| Código | Tipo |
|---|---|
| `0` | Pared |
| `1` | Espacio vacío |
| `2` | Destino |
| `3` | Piso resbaladizo |
| `4` | Cerrojo |
| `5` | Muro cerrado |
| `6` | Muro abierto |
| `7` | Pared destructible |

**Códigos de entidad:**

| Código | Tipo |
|---|---|
| `0` | Ninguna |
| `1` | Jugador |
| `2` | Caja normal |
| `3` | Caja frágil |
| `4` | Caja llave |
| `5` | Caja explosiva |
| `6` | Spray acuático |

`MovMin` indica la cantidad mínima de movimientos para completar el nivel (se usa para el cálculo del puntaje).
