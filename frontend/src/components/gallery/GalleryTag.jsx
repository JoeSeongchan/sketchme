import { React, useState, useEffect } from 'react';

function GalleryTag({ tags, onTagChange }) {
  const [localTags, setLocalTags] = useState(tags);

  useEffect(() => {
    // localTags가 변경될 때마다 부모 컴포넌트로 변경된 태그 배열 전달
    onTagChange(localTags);
  }, [localTags]);

  const handleClick = (buttonInfo) => {
    if (localTags.some((tag) => tag.hashtagID === buttonInfo.hashtagID)) {
      setLocalTags(localTags.filter((tag) => tag.hashtagID !== buttonInfo.hashtagID));
    } else {
      setLocalTags([...localTags, buttonInfo]);
    }
  };

  const isSelected = (buttonInfo) => (localTags.some((tag) => tag.hashtagID === buttonInfo.hashtagID) ? 'bg-primary_2 rounded-lg text-white' : '');
  const themeButton = [
    { hashtagID: 1, name: '1인' },
    { hashtagID: 2, name: '커플' },
    { hashtagID: 3, name: '가족' },
    { hashtagID: 4, name: '반려동물' },
    { hashtagID: 5, name: '효도' },
    { hashtagID: 6, name: '기념일' },
    { hashtagID: 7, name: '사실적인' },
  ];
  const vibeButton = [
    { hashtagID: 8, name: '따뜻한' },
    { hashtagID: 9, name: '귀여운' },
    { hashtagID: 10, name: '웃긴' },
    { hashtagID: 11, name: '산뜻한' },
    { hashtagID: 12, name: '즐거운' },
    { hashtagID: 13, name: '자연스러운' },
  ];
  // const priceButton = [
  //   { hashtagID: 14, name: '~1000' },
  //   { hashtagID: 15, name: '1000 ~ 5000' },
  //   { hashtagID: 16, name: '5000 ~ 10000' },
  //   { hashtagID: 17, name: '10000 ~ 50000' },
  //   { hashtagID: 18, name: '50000 ~' },
  // ];

  const generateButtonGrid = (buttons) => (
    <div className="flex">
      {buttons.map((button) => (
        <button
          key={button.hashtagID}
          type="button"
          onClick={() => handleClick(button)}
          className={`bg-transparent text-darkgrey rounded-xl hover:bg-primary_2 hover:text-white 2xl:mx-4 xl:mx-2 lg-mx-1 p-2 my-2 ${isSelected(button)}`}
        >
          {button.name}
        </button>
      ))}
    </div>

  );

  return (
    <div className="w-4/5 mx-auto mb-5">
      <div className="mx-auto mt-10">
        <div className="rounded-xl shadow-xl grid grid-cols-2 font-semibold justify-center items-stretch text-center" style={{ gridTemplateColumns: '80px 3fr' }}>
          <div className="flex rounded-tl-xl items-center border-b border-grey justify-center bg-lightgrey">
            테마
          </div>
          <div className="border-b border-grey flex flex-col">
            {generateButtonGrid(themeButton)}
          </div>
          <div className="flex items-center justify-center border-b border-grey bg-lightgrey">
            분위기
          </div>
          <div className="flex flex-col border-b border-grey">
            {generateButtonGrid(vibeButton)}
          </div>
        </div>
      </div>
    </div>
  );
}

export default GalleryTag;
