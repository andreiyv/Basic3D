package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
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
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;


public class Basic3D extends InputAdapter implements ApplicationListener {
//public class Basic3D extends ApplicationAdapter implements InputProcessor {

    // Тип для массива инстансов моделей
    public static class GameObject extends ModelInstance {
        public final Vector3 center = new Vector3();
        public final Vector3 dimensions = new Vector3();
        public final float radius;

        private final static BoundingBox bounds = new BoundingBox();

        public GameObject(Model model, String rootNode, boolean mergeTransform) {
            super(model, rootNode, mergeTransform);
            calculateBoundingBox(bounds);
            bounds.getCenter(center);
            bounds.getDimensions(dimensions);
            radius = dimensions.len() / 2f;
        }

        public GameObject(Model model, float x, float y, float z) {
            super(model, x, y, z);
            calculateBoundingBox(bounds);
            bounds.getCenter(center);
            bounds.getDimensions(dimensions);
            radius = dimensions.len() / 2f;
        }
    }

    public PerspectiveCamera cam;
    //public OrthographicCamera cam;
    public ModelBatch modelBatch;
    public Model model, table;
    public CameraInputController camController;
    protected Stage stage;
    protected Label label;
    protected BitmapFont font;
    protected StringBuilder stringBuilder;
    public Environment environment;
    private int visibleCount;
    private int selected = -1, selecting = -1;
    Vector3 cube_position;
    protected Array<GameObject> instances = new Array<GameObject>();

    protected boolean[] cube_up = {false, false, false, false, false, false, false, false, false, false};
    protected boolean[] cube_down = {false, false, false, false, false, false, false, false, false, false};
    protected boolean[] cube_rotate = {false, false, false, false, false, false, false, false, false, false};
    protected int[] cube_iter = {0,0,0,0,0,0,0,0,0,0};

    public int MAX_UP = 9;

    private Vector3 position = new Vector3();
    float delta_x, delta_y, delta_z;
    private Material selectionMaterial;
    private Material originalMaterial;

    DirectionalShadowLight shadowLight;
    ModelBatch shadowBatch;
    float delta = 0.0f;

    float centerX, centerY, centerZ;
    float y_base = 0.0f;

    boolean go_up = true;

    @Override
    public void create() {

        stage = new Stage();
        font = new BitmapFont();
        label = new Label(" ", new Label.LabelStyle(font, Color.WHITE));
        stage.addActor(label);
        stringBuilder = new StringBuilder();

        modelBatch = new ModelBatch();

/*
   Up Z, Right Y, X through the glass
 */
        cam = new PerspectiveCamera(90, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

      //  cam = new OrthographicCamera(30, 30 * (h / w));
      //  cam.position.set(0f, 20f, 0f);
      //  cam.lookAt(0,0,0);
      //  cam.update();

        cam.position.set(-1.1f, 14f, 0.0f);
        cam.lookAt(-0.4f, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.5f, 0.5f, 0.5f, 1f));
        environment.add((shadowLight = new DirectionalShadowLight(1024, 1024, 30f, 30f, 1f, 100f)).set(0.8f, 0.8f, 0.8f, -1f, -.8f, -.2f));
        environment.shadowMap = shadowLight;

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(new InputMultiplexer(this, camController));

        SceneBuilder sceneBuilder = new SceneBuilder();

        model = new Model();

        model = sceneBuilder.build();

        ModelBuilder tableBuilder = new ModelBuilder();

        tableBuilder.begin();
        Node node1 = tableBuilder.node();
        node1.id = "table";
        MeshPartBuilder tableMesh;
        tableMesh = tableBuilder.part("top", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates, new Material(ColorAttribute.createDiffuse(Color.BLUE)));
        tableMesh.box(14.0f, 0.1f, 14.f);

        table = tableBuilder.end();

        instances.add(new GameObject(table, 0f, 0f, 0f));

        instances.get(0).transform.trn(-2.0f, 0, -1.5f);

        for (int i = -2; i < 1; i++) {
            for (int k = -2; k < 1; k++) {
                instances.add(new GameObject(model, i * 3 + 0.1f, 0f, k * 3 + 0.1f));
            }
        }


        shadowBatch = new ModelBatch(new DepthShaderProvider());

        selectionMaterial = new Material();
        selectionMaterial.set(ColorAttribute.createDiffuse(Color.ORANGE));
        originalMaterial = new Material();

    }

    @Override
    public void render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camController.update();

        modelBatch.begin(cam);

        visibleCount = 0;

        shadowLight.begin(Vector3.Zero, cam.direction);
        shadowBatch.begin(shadowLight.getCamera());

        for (int i = 0; i < instances.size; i++) {

            Vector3 position = instances.get(i).transform.getTranslation(new Vector3());

            centerX = position.x;
            centerY = position.y;
            centerZ = position.z;

            if (isVisible(cam, instances.get(i))) {

                shadowBatch.render(instances.get(i));

                modelBatch.render(instances.get(i), environment);

                visibleCount++;

            }
//            https://xoppa.github.io/blog/behind-the-3d-scenes-part2/

            if (instances.get(i).getNode("table") == null) {

 /*               if (cube_up[i] && cube_iter[i] < MAX_UP) {
                    delta = MAX_UP/30.0f;
                    cube_iter[i] = cube_iter[i] + 1;
                    instances.get(i).transform.trn(0, delta, 0);
                }

                if (cube_up[i] && cube_iter[i] == (MAX_UP -1) ) cube_up[i] = false;

                if (cube_down[i]  && cube_iter[i] > 0) {
                    delta = -MAX_UP/30.0f;
                    cube_iter[i] = cube_iter[i] - 1;
                    instances.get(i).transform.trn(0, delta, 0);
                }

                if (cube_down[i] && cube_iter[i] == 0) cube_down[i] = false;
*/

                if (cube_rotate[i]) {

                    if (cube_position.x >= 0 ) {
                        delta_x = -cube_position.x;
                    } else {
                        delta_x = cube_position.x;
                    }

                    if (cube_position.y >= 0 ) {
                        delta_y = -cube_position.y;
                    } else {
                        delta_y = cube_position.y;
                    }

                    if (cube_position.z >= 0 ) {
                        delta_z = -cube_position.z;
                    } else {
                        delta_z = cube_position.z;
                    }

                    BoundingBox cube_bounds = new BoundingBox();
                    instances.get(i).calculateBoundingBox(cube_bounds);

                    instances.get(i).transform.translate(-cube_position.x+cube_bounds.getCenterX(),
                            -cube_position.y+cube_bounds.getCenterY(),
                            -cube_position.z+cube_bounds.getCenterZ() );

                    Vector3 cube_position_m = instances.get(i).transform.getTranslation(new Vector3());

                    instances.get(i).transform.rotate(1,0,0, 1.5f);

                    instances.get(i).transform.translate(cube_position.x-cube_bounds.getCenterX(),
                            cube_position.y-cube_bounds.getCenterY(),
                            cube_position.z-cube_bounds.getCenterZ());

                 //   instances.get(i).transform.rotate(1,0,0,1.5f);

                }

            }

        }

        shadowBatch.end();
        shadowLight.end();

        modelBatch.end();

        stringBuilder.setLength(0);
       // stringBuilder.append(" FPS: ").append(Gdx.graphics.getFramesPerSecond());
        //  stringBuilder.append(" Visible: ").append(visibleCount);
        //  stringBuilder.append(" Selected: ").append(selected);
        stringBuilder.append("d_x: ").append(delta_x);
        stringBuilder.append("d_y: ").append(delta_y);
        stringBuilder.append("d_z: ").append(delta_z);
        label.setText(stringBuilder);
        stage.draw();

    }

    protected boolean isVisible(final Camera cam, final GameObject instance) {
        instance.transform.getTranslation(position);
        position.add(instance.center);
        return cam.frustum.sphereInFrustum(position, instance.radius);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        selecting = getObject(screenX, screenY);
        //    System.out.println("***** touchDown\n");
        if (selecting > 0 && selecting < 10) {
            if (cube_iter[selecting] == (MAX_UP - 1)) cube_down[selecting] = true;
            if (cube_iter[selecting] == 0) cube_up[selecting] = true;
            cube_position = instances.get(selecting).transform.getTranslation(new Vector3());
        }
        return selecting > 0;
        // return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (selecting >= 0) {
            if (selecting == getObject(screenX, screenY)) setSelected(selecting);
            cube_rotate[selecting] = false;
            selecting = -1;
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        if (selecting < 0) return false;
        if (selected == selecting) {
            cube_up[selected] = false;
            cube_down[selected] = false;
            cube_rotate[selected] = true;
            Ray ray = cam.getPickRay(screenX, screenY);
            final float distance = -ray.origin.y / ray.direction.y;
            position.set(ray.direction).scl(distance).add(ray.origin);





        }
        return true;
    }

    public void setSelected(int value) {
        if (selected == value) return;
        if (selected >= 0) {
            Material mat = instances.get(selected).materials.get(0);
            mat.clear();
            mat.set(originalMaterial);
        }
        selected = value;
        if (selected >= 0) {
            Material mat = instances.get(selected).materials.get(0);
            originalMaterial.clear();
            originalMaterial.set(mat);
            mat.clear();
            mat.set(selectionMaterial);
        }
    }

    public int getObject(int screenX, int screenY) {
        Ray ray = cam.getPickRay(screenX, screenY);
        int result = -1;
        float distance = -1;
        for (int i = 0; i < instances.size; ++i) {
            final GameObject instance = instances.get(i);
            instance.transform.getTranslation(position);
            position.add(instance.center);
            final float len = ray.direction.dot(position.x - ray.origin.x, position.y - ray.origin.y, position.z - ray.origin.z);
            if (len < 0f)
                continue;
            float dist2 = position.dst2(ray.origin.x + ray.direction.x * len, ray.origin.y + ray.direction.y * len, ray.origin.z + ray.direction.z * len);
            if (distance >= 0f && dist2 > distance)
                continue;
            if (dist2 <= instance.radius * instance.radius) {
                result = i;
                distance = dist2;
            }
        }
        return result;
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        instances.clear();
        //  assets.dispose();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }


    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
