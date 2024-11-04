import React, { useEffect, useRef, useState } from "react";
import { GLTFLoader } from "three/examples/jsm/loaders/GLTFLoader";
import {
  Scene,
  PerspectiveCamera,
  WebGLRenderer,
  AmbientLight,
  DirectionalLight,
} from "three";

interface GLTFViewerProps {
  gltfUrl: string;
}

export const GLTFViewer: React.FC<GLTFViewerProps> = ({ gltfUrl }) => {
  const [isLoading, setIsLoading] = useState(true);
  const rendererRef = useRef<HTMLDivElement>(null);
  const rendererInstance = useRef<WebGLRenderer | null>(null);

  useEffect(() => {
    const scene = new Scene();
    const camera = new PerspectiveCamera(
      75,
      window.innerWidth / window.innerHeight,
      0.1,
      1000
    );
    camera.position.set(0, 1, 5);

    // 렌더러 초기화
    const renderer = new WebGLRenderer();
    renderer.setSize(window.innerWidth, window.innerHeight);
    rendererInstance.current = renderer;
    rendererRef.current?.appendChild(renderer.domElement);

    // 조명 추가
    const ambientLight = new AmbientLight(0x404040);
    scene.add(ambientLight);
    const directionalLight = new DirectionalLight(0xffffff, 0.5);
    scene.add(directionalLight);

    // GLTF 모델 로드
    const loader = new GLTFLoader();
    loader.load(
      gltfUrl,
      (gltf) => {
        scene.add(gltf.scene);
        animate();
        setIsLoading(false);
      },
      undefined,
      (error) => {
        console.error("GLTF 모델 로드 오류:", error);
        setIsLoading(false);
      }
    );

    // 렌더 루프
    const animate = () => {
      requestAnimationFrame(animate);
      renderer.render(scene, camera);
    };
    animate();

    // 창 크기 조정 핸들링
    const handleResize = () => {
      renderer.setSize(window.innerWidth, window.innerHeight);
      camera.aspect = window.innerWidth / window.innerHeight;
      camera.updateProjectionMatrix();
    };
    window.addEventListener("resize", handleResize);

    // 컴포넌트 언마운트 시 클린업
    return () => {
      window.removeEventListener("resize", handleResize);
      rendererInstance.current?.dispose();
      scene.clear();
    };
  }, [gltfUrl]);

  return (
    <div>
      {isLoading && <p>모델 로딩 중...</p>}
      <div>test</div>
      <div ref={rendererRef} className="w-full h-full" />
    </div>
  );
};

export default GLTFViewer;
