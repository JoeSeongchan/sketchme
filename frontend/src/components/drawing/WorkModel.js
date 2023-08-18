const WorkModel = (newId, newMode, newSize, newColor) => ({
  id: newId,
  mode: newMode, // 'brush' | 'eraser' | 'paint'
  size: newSize,
  color: newColor, // {r: 0, g: 0, b: 0, a: 1,}
  line: [],
});

export default WorkModel;
