import React from "react";
// import { Canvas, useFrame } from "@react-three/fiber";
// import { PerspectiveCamera, CameraControls, useGLTF } from "@react-three/drei";
// import { Group } from "three"; // Import Group type

interface GLTFViewerProps {
  gltfUrl: string;
}

export const GLTFViewer: React.FC<GLTFViewerProps> = ({ gltfUrl }) => {
  // const groupRef = useRef<Group>(null);
  // const { nodes, materials } = useGLTF(gltfUrl);
  // const [loading, setLoading] = useState(true);

  // Update loading state based on the GLTF loading status
  // if (!nodes || !materials) {
  //   return <div>모델 로딩 중...</div>; // Loading message
  // }

  return <>{gltfUrl}</>;
};

export default GLTFViewer;
