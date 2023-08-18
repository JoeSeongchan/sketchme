import { React, useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import SearchTab from './SearchTab';
import Tag from '../../components/search/Tag';
import {
  addSelectedButton,
  removeSelectedButton,
  removeAllSelectedButtons,
} from '../../reducers/SearchSlice';

function SearchPage() {
  const dispatch = useDispatch();
  const selectedButtons = useSelector((state) => state.search.selectedButtons);
  const [page, setPage] = useState(1);

  useEffect(
    () => () => {
      if (selectedButtons.length > 0) {
        dispatch(removeAllSelectedButtons());
      }
    },
    [],
  );

  const handleClick = (buttonInfo) => {
    if (selectedButtons.includes(buttonInfo)) {
      dispatch(removeSelectedButton(buttonInfo));
    } else {
      dispatch(addSelectedButton(buttonInfo));
    }
  };

  const isSelected = (buttonInfo) => (selectedButtons.includes(buttonInfo) ? 'bg-primary_2 rounded-lg text-white' : '');
  const themeButton = ['1인', '커플', '가족', '반려동물', '효도', '기념일', '사실적인'];
  const vibeButton = ['따뜻한', '귀여운', '웃긴', '산뜻한', '즐거운', '자연스러운'];
  const priceButton = ['~ 1000', '1000 ~ 5000', '5000 ~ 10000', '10000 ~ 50000', '50000 ~'];

  // Reusable function to generate grid rows for buttons

  const generateButtonGrid = (buttons) => (
    <div className="flex">
      {buttons.map((buttonText) => (
        <button
          key={buttonText}
          type="button"
          onClick={() => handleClick(buttonText)}
          className={`bg-transparent text-darkgrey rounded-xl hover:bg-primary_2 hover:text-white 2xl:mx-4 xl:mx-2 lg-mx-1 p-2 my-2 ${isSelected(buttonText)}`}
        >
          {buttonText}
        </button>
      ))}
    </div>

  );

  return (
    <div className="w-3/4 mx-auto">
      <div className="w-2/3 mx-auto mt-10">
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
          <div className="flex items-center rounded-bl-xl  border-grey justify-center bg-lightgrey">
            가격
          </div>
          <div className="border-darkgrey flex flex-col">
            {generateButtonGrid(priceButton)}
          </div>
        </div>
        <div className="flex justify-between items-center flex-wrap">
          <div className="flex flex-wrap" style={{ maxWidth: '1030px' }}>
            {selectedButtons.map((button, index) => (
              // eslint-disable-next-line react/no-array-index-key
              <Tag key={index} message={button} onClick={() => handleClick(button)} />
            ))}
          </div>
        </div>
      </div>
      <SearchTab currentPage={page} setPage={setPage} priceButtons={priceButton} />
    </div>
  );
}

export default SearchPage;
