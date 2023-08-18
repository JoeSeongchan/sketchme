import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { ReactComponent as Dotbogi } from '../../assets/icons/Dotbogi.svg';

function SearchBar() {
  const [searchValue, setSearchValue] = useState('');
  const [category, setCategory] = useState('pic'); // 드롭다운에서 선택한 값을 상태로 관리
  const navigate = useNavigate();

  const handleKeyPress = (event) => {
    if (event.key === 'Enter') {
      // 엔터키를 누를 때 '/artist/value' 또는 '/pic/value'로 이동
      navigate(`/search/${category}/?keyword=${searchValue}&orderBy=recent`);
    }
  };

  const handleInputChange = (event) => {
    setSearchValue(event.target.value);
  };

  return (
    <div className="flex items-center mr-3 ">
      <div className="flex items-center flex-1 border rounded-lg py-1 pe-1">
        <select
          className="px-2 border-r border-darkgrey focus:outline-none"
          defaultValue="pic"
          onChange={(e) => setCategory(e.target.value)}
        >
          <option value="pic">그림</option>
          <option value="artist">작가</option>
        </select>
        <input
          type="text"
          className="px-2 focus:outline-none"
          placeholder="검색어를 입력하세요"
          value={searchValue}
          onChange={handleInputChange}
          onKeyDown={handleKeyPress}
        />
        {/* 검색 버튼 */}
        <Link to={`/search/${category}/?keyword=${searchValue}&orderBy=recent`} className="mr-1">
          <button type="button" className="rounded-full pt-1 focus:outline-none">
            <Dotbogi className="w-5 h-5" />
          </button>
        </Link>
      </div>
    </div>
  );
}

export default SearchBar;
