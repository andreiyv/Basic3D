package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;


public class Basic3D implements ApplicationListener {
    public PerspectiveCamera cam;
    public ModelBatch modelBatch;
    public Model model;
    public ModelInstance instance;
    public CameraInputController camController;

    @Override
    public void create() {
        modelBatch = new ModelBatch();

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(1f, 1f, 1f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        ModelBuilder modelBuilder = new ModelBuilder();

        modelBuilder.begin();
        MeshPartBuilder tileBuilder;
        tileBuilder = modelBuilder.part("top", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates, new Material(ColorAttribute.createDiffuse(Color.RED)));
        tileBuilder.rect(-0.45f, 0.1f, 0.45f,   0.45f, 0.1f, 0.45f,    0.45f, 0.1f, -0.45f,  -0.45f, 0.1f, -0.45f,  0f, 1f, 0f);
        tileBuilder = modelBuilder.part("others", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GREEN)));
        tileBuilder.rect(-0.45f, 0f, -0.45f,    0.45f, 0f, -0.45f,     0.45f, 0f, 0.45f,     -0.45f, 0f,  0.45f,    0f, -1f, 0f);
        tileBuilder.rect(-0.45f, 0.1f, 0.45f,  -0.45f, 0f, 0.45f,      0.45f, 0f, 0.45f,      0.45f, 0.1f,  0.45f,    0f, 0f, 1f);
        tileBuilder.rect(-0.45f, 0.1f, 0.45f,  -0.45f, 0.1f, -0.45f,  -0.45f, 0f, -0.45f,    -0.45f, 0f,  0.45f,    -1f, 0f, 0f);
        tileBuilder.rect( 0.45f, 0.1f, 0.45f,   0.45f, 0f, 0.45f,      0.45f, 0f, -0.45f,     0.45f, 0.1f,  -0.45f,    0f, 0f, 1f);
        tileBuilder.rect(-0.45f, 0.1f, -0.45f,  0.45f, 0.1f, -0.45f,   0.45f, 0f, -0.45f,    -0.45f, 0f,  -0.45f,    0f, 0f, 1f);

        model = modelBuilder.end();

        instance = new ModelInstance(model);


    }

    @Override
    public void render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(cam);
        modelBatch.render(instance);
        modelBatch.end();
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
