import React from 'react';
import PropTypes from 'prop-types';
import OvVideo from './ovVideo';

function UserVideo({ streamManager }) {
  // const [thisManager, setThisManager] = useState(streamManager);

  const getNicknameTag = () => JSON.parse(streamManager.stream.connection.data).clientData;

  return (
    <div>
      {streamManager !== undefined ? (
        <div className="streamcomponent">
          <OvVideo streamManager={streamManager} />
          <div>
            <p>{getNicknameTag()}</p>
          </div>
        </div>
      ) : null}
    </div>
  );
}

// 나중에 타입 수정할 것
UserVideo.propTypes = {
  streamManager: PropTypes.node.isRequired,
};

export default UserVideo;
