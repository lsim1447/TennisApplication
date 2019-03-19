export const getPageY = () => {
    if (window.pageYOffset !== undefined) {
        return window.pageYOffset;
    }
    const isCSS1Compat = ((document.compatMode || '') === 'CSS1Compat');
    return isCSS1Compat ? document.documentElement.scrollTop : document.body.scrollTop;
};

export const getPageX = () => {
    if (window.pageXOffset !== undefined) {
        return window.pageXOffset;
    }
    const isCSS1Compat = ((document.compatMode || '') === 'CSS1Compat');
    return isCSS1Compat ? document.documentElement.scrollLeft : document.body.scrollLeft;
};

export const isGrandSlam = (tournament) => {
    return (tournament.tourney.tourney_id === '520') || (tournament.tourney.tourney_id === '540') 
            || (tournament.tourney.tourney_id === '560') || (tournament.tourney.tourney_id === '580');
}

export const getGrandSlamsFromMatches = (matches) => {
    return matches.filter(match => isGrandSlam(match.tournament))
}

export const wonMatchesBy = (matches, player) => {
    return matches.filter(match => {
        return match.winnerPlayer.playerSlug === player.playerSlug;
    })
}

export const wonMatchesOn = (matches, surface) => {
    return matches.filter(match => {
        return match.tournament.tourney.surface === surface;
    })
}