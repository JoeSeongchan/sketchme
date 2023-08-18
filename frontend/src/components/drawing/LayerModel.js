const LayerModel = (newName, newId, newType) => ({
  visible: true,
  id: newId,
  name: newName,
  type: newType, // 'background' | 'rough' | 'layer'
});

export default LayerModel;
