import React, { useState } from 'react';
// import './commonCss.css';
// C:\Users\SSAFY\Desktop\GIT\S09P12A504\frontend\src\components\common\commonCss.css
function Tap() {
  const menuArr = [
    {
      index: 0,
      title: 'tap1',
      content: 'tab menu 1',
    },
    {
      index: 1,
      title: 'tap2',
      content: 'tab menu 2',
    },
    {
      index: 2,
      title: 'tap3',
      content: 'tab menu 3',
    },
    {
      index: 3,
      title: 'tap4',
      content: 'tab menu 4',
    },
  ];

  const [index, setIndex] = useState(0);

  // const tabCss=

  const unselected = 'my-2 block border-x-0 border-t-0 border-transparent px-7 pb-3.5 pt-4 text-xs font-medium uppercase leading-tight text-neutral-500 hover:isolate hover:border-transparent hover:bg-prmiary_4 focus:isolate focus:border-transparent';
  const selected = `${unselected} border-b-2 border-primary text-primary`;
  return (
    <div>
      <ul className="mb-5 flex list-none flex-row flex-wrap border-b-0 pl-0">
        {menuArr.map((item) => (
          <li key={item.index}>
            <button
              type="button"
              onClick={() => setIndex(item.index)}
              className={item.index === index ? selected : unselected}
            >
              {item.title}
            </button>
          </li>
        ))}
      </ul>
      <div>
        {menuArr
          .filter((item) => index === item.index)
          .map((item) => (
            <div>{item.content}</div>
          ))}
      </div>
    </div>
  );
}

export default Tap;
