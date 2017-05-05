package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;


public class Basic3D extends InputAdapter implements ApplicationListener {

// Тип для массива инстансов моделей
    public static class GameObject extends ModelInstance {
        public final Vector3 center = new Vector3();
        public final Vector3 dimensions = new Vector3();
        public final float radius;

        private final static BoundingBox bounds = new BoundingBox();

        public GameObject (Model model, String rootNode, boolean mergeTransform) {
            super(model, rootNode, mergeTransform);
            calculateBoundingBox(bounds);
            bounds.getCenter(center);
            bounds.getDimensions(dimensions);
            radius = dimensions.len() / 2f;
        }
    }





    public PerspectiveCamera cam;
    public ModelBatch modelBatch;
    public Model model;
    public ModelInstance simple_instance;
    public CameraInputController camController;
    protected Stage stage;
    protected Label label;
    protected BitmapFont font;
    protected StringBuilder stringBuilder;
    public Environment environment;
    private int visibleCount;
    private int selected = 1;
    protected Array<GameObject> instances = new Array<GameObject>();

    @Override
    public void create() {

        stage = new Stage();
        font = new BitmapFont();
        label = new Label(" ", new Label.LabelStyle(font, Color.WHITE));
        stage.addActor(label);
        stringBuilder = new StringBuilder();

        modelBatch = new ModelBatch();
/*

   Up Z, Right Y, X thru the glass
 */


        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(5f, 5f, 5f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.1f, 0.1f, 0.1f, 1f));
        environment.add(new DirectionalLight().set(1.8f, 1.8f, 1.8f, -1f, -0.8f, -0.2f));

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        ModelBuilder modelBuilder = new ModelBuilder();

        modelBuilder.begin();
        Node node1 = modelBuilder.node();
        node1.id = "1";
// MeshBuilder
        MeshPartBuilder tileBuilder;

        tileBuilder = modelBuilder.part("top", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates, new Material(ColorAttribute.createDiffuse(Color.RED)));
        tileBuilder.rect(-1f, 2f, 1f,   1f, 2f, 1f,    1f, 2f, -1f,  -1f, 2f, -1f,  0f, 1f, 0f);

        tileBuilder = modelBuilder.part("side1", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GREEN)));
        tileBuilder.rect(-1f, 0f, -1f,    1f, 0f, -1f,     1f, 0f, 1f,     -1f, 0f,  1f,    0f, -1f, 0f);

        tileBuilder = modelBuilder.part("side2", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.BLUE)));
        tileBuilder.rect(-1f, 2f, 1f,  -1f, 0f, 1f,      1f, 0f, 1f,      1f, 2f,  1f,    0f, 0f, 1f);

        tileBuilder = modelBuilder.part("side3", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.WHITE)));
        tileBuilder.rect(-1f, 2f, 1f,  -1f, 2f, -1f,  -1f, 0f, -1f,    -1f, 0f,  1f,    -1f, 0f, 0f);

        tileBuilder = modelBuilder.part("site4", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.YELLOW)));
        tileBuilder.rect( 1f, 2f, 1f,   1f, 0f, 1f,      1f, 0f, -1f,     1f, 2f,  -1f,    0f, 0f, 1f);

        tileBuilder = modelBuilder.part("site5", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.MAGENTA)));
        tileBuilder.rect(-1f, 2f, -1f,  1f, 2f, -1f,   1f, 0f, -1f,    -1f, 0f,  -1f,    0f, 0f, 1f);

        model = modelBuilder.end();

        simple_instance = new ModelInstance(model);

        GameObject instance = new GameObject(model, model.nodes.get(0).id, true);
        instances.add(instance);
    }

    @Override
    public void render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camController.update();
        modelBatch.begin(cam);
        for (final GameObject instance : instances) {
            //      if (isVisible(cam, instance)) {
            modelBatch.render(instance, environment);
           // modelBatch.render(simple_instance, environment);
            //        visibleCount++;
     //   }
            visibleCount = 1;
    }
        modelBatch.end();
        stringBuilder.setLength(0);
        stringBuilder.append(" FPS: ").append(Gdx.graphics.getFramesPerSecond());
        stringBuilder.append(" Visible: ").append(visibleCount);
        stringBuilder.append(" Selected: ").append(selected);
        label.setText(stringBuilder);
        stage.draw();

    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        model.dispose();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
