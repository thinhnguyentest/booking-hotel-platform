import React from 'react';
import './authorCard.css'; // Import file CSS

const AuthorCard = () => {
    return (
        <div className="author-card">
            <div className="author-info">
                <img
                    className="author-avatar"
                    src="https://d41chssnpqdne.cloudfront.net/user_upload_by_module/chat_bot/files/82189916/7FcWqvQESHh7F3YX.png?Expires=1741941641&Signature=kGWe2ua4g9oJX5tI23ENuNW7cW7n6YZUPS3i6Hagavh0ZGRje12JqLRAbOt0uLPZvuWsKVwVZnWhJ~qVJD3d6FK-H3LSJ44buAuhuZFVrQx1MFW~JeGbXBfQLvbczfHiVvBlT7joHdQaOfVcAW2ilgWR9YSdprS8hjR9rS3ACiAxAH9OOKAdOCuSLnYXaXKn6kMm1g56ICZyXZQLQmVFEhW4qWIvAjQc5PkXXsUUHx8gPfx6A84V3dVOeSenUYLme7hgFFoPBJo913IX~-Vd9FqHqyCTaWL7XM6-Psnn88j3ucEi7s4yd~rfHM-KM4qemil94iqpxGP2jM02nFj3DQ__&Key-Pair-Id=K3USGZIKWMDCSX"
                    alt="Falconar Agnes"
                />
                <div className="author-details">
                    <h2 className="author-name">Falconar Agnes</h2>
                </div>
            </div>
        </div>
    );
};

export default AuthorCard;