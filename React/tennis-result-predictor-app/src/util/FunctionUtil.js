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