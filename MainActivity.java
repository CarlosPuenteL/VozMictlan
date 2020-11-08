package com.example.mitlan;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.Normalizer2;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.TransformableNode;


public class MainActivity extends AppCompatActivity {
    private ArFragment arFragment;
    private ModelRenderable modelRenderable;
    private String MODEL_URL = "https://github.com/Tartolas/tartolas1/blob/main/Tigre.glb?raw=true";

   //https://github.com/Tartolas/tartolas1/blob/main/Tigre.glb
    // https://github.com/Tartolas/tartolas1/blob/main/model.gltf?=raw=true
   // https://github.com/Tartolas/tartolas1/blob/main/alduin-dragon.glb
   // https://github.com/Tartolas/tartolas1/blob/main/AnyConv.com__oni.glb
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        setUpModel();
        setUpPlane();
    }

    private void setUpPlane() {
        arFragment.setOnTapArPlaneListener((((hitResult, plane, motionEvent) -> {
            Anchor anchor = hitResult.createAnchor();
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(arFragment.getArSceneView().getScene());
            createModel(anchorNode);
        })));
    }

    private void createModel(AnchorNode anchorNode){
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setParent(anchorNode);
        node.setRenderable(modelRenderable);
        node.select();
    }

    private void setUpModel() {
        ModelRenderable.builder()
                .setSource(this,
                        RenderableSource.builder().setSource(
                                this,
                                Uri.parse(MODEL_URL),
                                RenderableSource.SourceType.GLB)
                        .setScale(0.5f)
                        .build())
                .setRegistryId(MODEL_URL)
                .build()
                .thenAccept(renderable -> modelRenderable = renderable)
                .exceptionally(throwable -> {
                    Toast.makeText(MainActivity.this,"No carga",Toast.LENGTH_SHORT).show();
                    return null;
                } );

    }
}