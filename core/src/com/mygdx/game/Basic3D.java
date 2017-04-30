package com.mygdx.game;

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
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;



    public class Basic3D implements ApplicationListener {
        public PerspectiveCamera cam;
        public ModelBatch modelBatch;
        public Model model1, model2;
        public ModelInstance instance1, instance2;
        public CameraInputController camController;
        protected Environment environment;

        @Override
        public void create() {
            modelBatch = new ModelBatch();

            modelBatch = new ModelBatch();
            environment = new Environment();
            environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
            environment.add(new DirectionalLight().set(0.8f, 15f, 0.8f, -1f, -0.8f, -0.2f));
            environment.add(new DirectionalLight().set(15f, 0.8f, 0.8f, 0.01f, -0.1f, -0.1f));

            cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            cam.position.set(10f, 10f, 10f);
            cam.lookAt(0,0,0);
            cam.near = 1f;
            cam.far = 300f;
            cam.update();

            ModelBuilder modelBuilder = new ModelBuilder();
            model1 = modelBuilder.createBox(5f, 5f, 5f,
                    new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                    Usage.Position | Usage.Normal);

            model2 = modelBuilder.createCone(5f, 15f, 5f,10,
                    new Material(ColorAttribute.createDiffuse(Color.BLUE)),
                    Usage.Position | Usage.Normal);

            instance1 = new ModelInstance(model1);
            instance2 = new ModelInstance(model2);

            camController = new CameraInputController(cam);
            Gdx.input.setInputProcessor(camController);
        }

        @Override
        public void render() {
            Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

            modelBatch.begin(cam);
            modelBatch.render(instance1, environment);
            modelBatch.render(instance2, environment);
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
        }}
