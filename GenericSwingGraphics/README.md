## Introducing Generic Swing Graphics (GSG)

This is a tentative to use some graphical utilities to display graphics on a `JPanel`
as generically as possible, in a Swing application (in a JFrame for example), as well as in an iJava Jupyter Notebooks

> _Note:_ There is here quite an overlap with what `XChart` already provides.

User should only have to focus on the data to display, and the way to display them,
not on the Swing plumbing, not always straight-forward for some data scientists.

All the skills are in `gsg.SwingUtils.WhiteBoardPanel`, which in turn refers to `gsg.VectorUtils`.

See in the provided examples the relevant code:
- `gsg.examples.easy.SwingSample5` for a pure Swing application.
- In `Chapter 02`, `GSG_EM_Test_01.ipynb` for an IJava Notebook.

### Easy mode
Use the default `whiteBoardWriter` named `DEFAULT_DASHBOARD_WRITER`.

### Hands-on mode
Override the `whiteBoardWriter`.

TODO: A globe (earth)

---
