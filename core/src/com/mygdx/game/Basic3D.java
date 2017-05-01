package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;


public class Basic3D implements ApplicationListener {
    public PerspectiveCamera cam;
    public ModelBatch modelBatch;
    public Model model1, model2;
    public ModelInstance instance1, instance2;
    public CameraInputController camController;
    public Environment environment;
    public Model cube, cone, sphere, table;
    public Material mat_cube, mat_cone, mat_sphere, mat_table, mat_ground;
    public ModelInstance inst_cone, inst_building, inst_ground;

    @Override
    public void create() {
        modelBatch = new ModelBatch();
        mat_cube = new Material(ColorAttribute.createDiffuse(Color.GREEN));
        mat_cone = new Material(ColorAttribute.createDiffuse(Color.GREEN));
        mat_sphere = new Material(ColorAttribute.createDiffuse(Color.BLUE));
        mat_table = new Material(ColorAttribute.createDiffuse(Color.WHITE));
        mat_ground = new Material(ColorAttribute.createDiffuse(Color.GRAY));

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));


        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        ModelBuilder modelBuilder = new ModelBuilder();

        modelBuilder.begin();

        Node node1 = modelBuilder.node();
        node1.id = "node1";
        node1.translation.set(0f, 2.8f, 0f);
        MeshPartBuilder meshBuilder;
        meshBuilder = modelBuilder.part("part1", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, mat_ground);
        meshBuilder.box(9f, 0.1f, 9f);

        Model ground = modelBuilder.end();

        modelBuilder.begin();

            node1 = modelBuilder.node();
            node1.id = "node1";
            node1.translation.set(0f, 2.8f, 0f);
 //               MeshPartBuilder meshBuilder;
                meshBuilder = modelBuilder.part("part1", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, mat_cone);
                meshBuilder.box(1, 5, 1);

        Model building = modelBuilder.end();



        inst_ground = new ModelInstance(ground);
        inst_building = new ModelInstance(building);

        inst_building.transform.scl(0.5f);

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);
    }

    @Override
    public void render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(cam);
        modelBatch.render(inst_building, environment);
        modelBatch.render(inst_ground, environment);
        modelBatch.end();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        model1.dispose();
        model2.dispose();
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
