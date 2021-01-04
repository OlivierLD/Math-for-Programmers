## Introducing Generic Swing Graphics (GSG)

This is a tentative to use some graphical utilities to display graphics on a `JPanel`
as generically as possible, in a Swing application (in a JFrame for example), as well as in an iJava Jupyter Notebooks

> _Note:_ There is here quite an overlap with what `XChart` already provides.

User should only have to focus on the data to display, and the way to display them,
not on the Swing plumbing, not always straight-forward for some data scientists.

All the skills are in `gsg.SwingUtils.WhiteBoardPanel`, which in turn refers to `gsg.VectorUtils`.

See in the provided examples the relevant code:
- `gsg.examples.easy.SwingSample5.java` for a pure Swing application.
- In `Chapter 02`, `GSG_EM_Test_01.ipynb` for an IJava Notebook.

### Easy mode
Use the default `whiteBoardWriter` named `DEFAULT_DASHBOARD_WRITER`, and use the `addSerie` method on the `whiteBoard` instance:
```java
Vector2D translation = new Vector2D(x, y);
List<Vector2D> oneSmallDino = scaled.stream().map(v -> VectorUtils.translate(translation, v)).collect(Collectors.toList());

WhiteBoardPanel.DataSerie serie100 = new WhiteBoardPanel.DataSerie()
                            .data(oneSmallDino)
                            .graphicType(WhiteBoardPanel.GraphicType.CLOSED_LINE)
                            .lineThickness(1)
                            .color(Color.BLACK);
whiteBoard.addSerie(serie100);
```

### Under-the-hood mode
Override the `whiteBoardWriter`.

A globe (earth), see `gsg.examples.override.SwingGlobeSample.java`.
> _Note_: This globe example requires the jar file obtained by the build of the `chartcomponents` module of
> the `olivsoft-swing` repo at <https://github.com/OlivierLD/olivsoft-swing>. You need to run
```
 chartcomponent $ ../gradlew clean shadowJar --parallel
```
> and then, copy the `chartcomponents-4.0.0.0-all.jar` at the root of this repo.

---
