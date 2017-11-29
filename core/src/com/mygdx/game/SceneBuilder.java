package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

/**
 * Created by voran on 5/12/17.
 */

public class SceneBuilder {

    public SceneBuilder(){

    }

    public Model build() {

        Model model;

        // start to build model
        ModelBuilder modelBuilder = new ModelBuilder();

        modelBuilder.begin();

        Node node1 = modelBuilder.node();
        node1.id = "n1";

        MeshPartBuilder tileBuilder;

        tileBuilder = modelBuilder.part("top", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates, new Material(ColorAttribute.createDiffuse(Color.RED)));
        tileBuilder.rect(-1f, 2f, 1f, 1f, 2f, 1f, 1f, 2f, -1f, -1f, 2f, -1f, 0f, 1f, 0f);

        tileBuilder = modelBuilder.part("side1", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GREEN)));
        tileBuilder.rect(-1f, 0f, -1f, 1f, 0f, -1f, 1f, 0f, 1f, -1f, 0f, 1f, 0f, -1f, 0f);

        tileBuilder = modelBuilder.part("side2", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.BLUE)));
        tileBuilder.rect(-1f, 2f, 1f, -1f, 0f, 1f, 1f, 0f, 1f, 1f, 2f, 1f, 0f, 0f, 1f);

        tileBuilder = modelBuilder.part("side3", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.WHITE)));
        tileBuilder.rect(-1f, 2f, 1f, -1f, 2f, -1f, -1f, 0f, -1f, -1f, 0f, 1f, -1f, 0f, 0f);

        tileBuilder = modelBuilder.part("site4", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.YELLOW)));
        tileBuilder.rect(1f, 2f, 1f, 1f, 0f, 1f, 1f, 0f, -1f, 1f, 2f, -1f, 0f, 0f, 1f);

        tileBuilder = modelBuilder.part("site5", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.MAGENTA)));
        tileBuilder.rect(-1f, 2f, -1f, 1f, 2f, -1f, 1f, 0f, -1f, -1f, 0f, -1f, 0f, 0f, 1f);

        model = modelBuilder.end();
        
        return model;
    }
}
