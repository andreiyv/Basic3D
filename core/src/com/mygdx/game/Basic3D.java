package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;


public class Basic3D implements ApplicationListener {
        public PerspectiveCamera cam;
        public ModelBatch modelBatch;
        public Model model;
        public ModelInstance instance;
        public CameraInputController camController;
        public Renderable renderable;
        public RenderContext renderContext;
        public Shader shader;

        @Override
        public void create() {
       //     modelBatch = new ModelBatch();

            cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            cam.position.set(10f, 10f, 10f);
            cam.lookAt(0,0,0);
            cam.near = 1f;
            cam.far = 300f;
            cam.update();

            camController = new CameraInputController(cam);
            Gdx.input.setInputProcessor(camController);

            ModelBuilder modelBuilder = new ModelBuilder();
            model = modelBuilder.createBox(5f, 5f, 5f,
                    new Material(),
                    Usage.Position | Usage.Normal | Usage.TextureCoordinates);

//             model = modelBuilder.createSphere(2f, 2f, 2f, 20, 20,
//                    new Material(),
//                    Usage.Position | Usage.Normal | Usage.TextureCoordinates);

            NodePart blockPart = model.nodes.get(0).parts.get(0);

            renderable = new Renderable();
            blockPart.setRenderable(renderable);
            renderable.environment = null;
            renderable.worldTransform.idt();

    //        renderable.meshPart.primitiveType = GL20.GL_POINTS;


            renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 1));

            String vert = Gdx.files.internal("test.vertex.glsl").readString();
            String frag = Gdx.files.internal("test.fragment.glsl").readString();
            shader = new DefaultShader(renderable, new DefaultShader.Config(vert, frag));
            shader.init();

            //instance = new ModelInstance(model);


        }

        @Override
        public void render() {
            camController.update();

            Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

            renderContext.begin();
            shader.begin(cam, renderContext);
            shader.render(renderable);
            shader.end();
            renderContext.end();
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
        }}
